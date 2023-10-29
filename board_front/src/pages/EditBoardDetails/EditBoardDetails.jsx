import React from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { useState } from 'react';
import { useEffect } from 'react';
import { useQuery, useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';
import { useNavigate, useParams } from 'react-router-dom';
import Select from 'react-select';
import ReactQuill from 'react-quill';

const titleInput = css`
    width: 100%;
    height: 40px;
    margin-bottom: 10px;
`;

const selectBox = css`
    flex-grow: 1;
    width: 70%;
`;

const categoryContainer = css`
    display: flex;
    margin-bottom: 10px;

    & > button {
        margin-left: 5px;
        height: 37px;
        background-color: #eee;
        border: none;
        border-radius: 7px;
        cursor: pointer;

        &:hover {
            background-color: #fff;
            border: 2px solid #eee;
        }
    }
`;

const buttonContainer = css`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 60px;
    width: 100%;

    & button {
        height: 23px;
        margin-left: 5px;
        background-color: #eee;
        border: none;
        border-radius: 7px;
        cursor: pointer;

        &:hover {
            background-color: #fff;
            border: 2px solid #eee;
        }
    }
`;

function EditBoardDetails(props) {
    const navigete = useNavigate();
    const [ boardContent, setBoardContent ] = useState({
        title: "",
        content: "",
        categoryId: "",
        boardId: ""
    });
    const [ newCategory, setNewCategory ] = useState("");
    const [ selectOptions, setSelectOptions ]= useState([]);
    const [ selectedOption, setSelectedOption ] = useState(selectOptions[0]);
    const { boardId } = useParams();
    const queryClient = useQueryClient();
    const [ board, setBoard ] = useState({});
    const [ quillRendered, setQuillRendered ] = useState(false);
    const getBoard = useQuery(["getBoard"], async () => {
        try {
            return await instance.get(`/board/${boardId}`);
        }catch(error) {

        }
    }, {
        refetchOnWindowFocus: false,
        onSuccess: response => {
            setBoard(response.data);
        }
    });

    useEffect(() => {
        instance.get("/board/categories").then((response) => {
            setSelectOptions(
                response.data.map(
                    category => {
                        return{ value: category.boardCategoryId, label: category.boardCategoryName }
                    }
                )
            )
        })
    }, [])

    useEffect(() => {
        if(!!newCategory){
            const newOption = { value: 0, label: newCategory }

            setSelectedOption(newOption);
            if(!selectOptions.map(option => option.label).includes(newOption.label)) {
                setSelectOptions([
                ...selectOptions,
                    newOption
                ]);
            }
        }
    }, [newCategory]);

    useEffect(() => {
        setBoardContent({
            ...boardContent,
            categoryId: selectedOption?.value,
            categoryName: selectedOption?.label,
            boardId: boardId
        })
    }, [selectedOption]);

    useEffect(() => {
        if (board && board.boardCategoryName) {
            const initialSelectedOption = selectOptions.find(option => option.label === board.boardCategoryName);
            setSelectedOption(initialSelectedOption);

            if (!boardContent.title && !boardContent.content) {
                setBoardContent({
                    ...boardContent,
                    title: board.boardTitle,
                    content: board.boardContent
                });
            }
            setQuillRendered(true);
        }
    }, [board, selectOptions]);

    const modules = {
        toolbar: {
            container: [
                [{header: [1, 2, 3, false]}],
                ["bold", "underline"],
                ["image"]
            ]
        }
    }

    if(getBoard.isLoading) {
        return <></>
    }

    const handleTitleInput = (e) => {
        setBoardContent({
            ...boardContent,
            title: e.target.value
        });
    }

    const handleContentInput = (value) => {
        setBoardContent({
            ...boardContent,
            content: value
        });
    }

    const handleSelectChange = (option) => {
        setSelectedOption(option);
    }

    const handleCategoryAdd = () => {
        const categoryName = window.prompt("새로 추가할 카테고리명을 입력하세요.");
        if(!categoryName) { // 비었으면
            return;
        }
        setNewCategory(categoryName);
    }

    const handleWriteSubmit = async () => {
        try {
            await instance.put(`/board/${boardId}/edit`, boardContent);
            alert("게시물 수정이 완료되었습니다.");
            queryClient.refetchQueries(["getPrincipal"]);
            navigete("/board/all/1");
            
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <RootContainer>
            <div>
                <h1>수정하기</h1>
                <div css={categoryContainer}>
                    <div css={selectBox}>
                    <Select options={selectOptions} onChange={handleSelectChange} value={selectedOption} />
                    </div>
                    <button onClick={handleCategoryAdd}>카테고리 추가</button>
                </div>
                <div><input css={titleInput} type="text" name='title' defaultValue={board.boardTitle} onChange={handleTitleInput} /></div>
                <div>
                    {quillRendered ? (
                        <ReactQuill style={{ width: "928px", height: "510px" }} modules={modules} onChange={handleContentInput} defaultValue={board.boardContent} />
                    ) : null}
                </div>
            </div>
            <div css={buttonContainer}>
                <button>취소</button>
                <button onClick={handleWriteSubmit}>수정</button>
            </div>
        </RootContainer>
    );
}
export default EditBoardDetails;