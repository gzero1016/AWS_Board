import React, { useEffect, useState } from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import ReactQuill from 'react-quill';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import Select from 'react-select';
import { instance } from '../../api/config/instance';
import { useQuery, useQueryClient } from 'react-query';

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

function BoardWrite(props) {
    const queryClient = useQueryClient();
    
    const [ boardContent, setBoardContent ] = useState({
        title: "",
        content: "",
        categoryId: 0,
        categoryName: ""
    });
    const [ newCategory, setNewCategory ] = useState("");
    const [ selectOptions, setSelectOptions ]= useState("");
    const [ selectedOption, setSelectedOption ] = useState(selectOptions[0]);
    const principalState = queryClient.getQueryState("getPrincipal");
    const principal = principalState.data.data;

    
    useEffect(() => {
        // 로그인안된거
        if(!principalState.data){
            alert("로그인 후 게시글을 작성하세요.");
            window.location.replace("/auth/signin");
            return;
        }

        // 인증이안된경우
        if(!principal?.enabled) {
            alert("이메일 인증 후 게시글을 작성하세요.");
            window.location.replace("/account/mypage");
            return;
        }
    }, [])

    // 해당 주소로 정상적으로 다녀왔다면~!
    // response의 data를 맵을 돌려서 category안에있는 name을 들고옴
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
            categoryName: selectedOption?.label
        })
    }, [selectedOption])

    const modules = {
        toolbar: {
            container: [
                [{header: [1, 2, 3, false]}],
                ["bold", "underline"],
                ["image"]
            ]
        }
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
        if (window.confirm("글 작성 시 500포인트가 차감됩니다. 작성하시겠습니까?")) {
            console.log(principal);
            if (principal?.userPoint >= 500) {
                const principalPoint = {
                    email: principal?.email,
                    point: 500
                };
                const option = {
                    headers:{
                        Authorization: localStorage.getItem("accessToken")
                    }
                }
                await instance.post("/board/content/point", principalPoint, option);
                try {
                    await instance.post("/board/content", boardContent, option);
                    alert("게시물 작성이 완료되었습니다.");
                    queryClient.refetchQueries(["getPrincipal"]);
                    window.location.replace("/board/all/1");
                } catch (error) {
                    console.log(error);
                }
            } else {
                if (window.confirm("포인트가 부족합니다. 충전하러 가시겠습니까?")) {
                    window.open("/store/products");
                } else {
                    alert("게시물 작성 실패");
                }
            }
        }
    }
    

    return (
        <RootContainer>
            <div>
                <h1>글쓰기</h1>
                <div css={categoryContainer}>
                    <div css={selectBox}>
                        <Select options={selectOptions} onChange={handleSelectChange} defaultValue={selectedOption} value={selectedOption}/>
                    </div>
                    <button onClick={handleCategoryAdd}>카테고리 추가</button>
                </div>
                <div><input css={titleInput} type="text" name='title' placeholder='제목' onChange={handleTitleInput} /></div>
                <div>
                    <ReactQuill style={{width: "928px", height: "510px"}} modules={modules} onChange={handleContentInput} />
                </div>
            </div>
            <div css={buttonContainer}>
                <button onClick={handleWriteSubmit}>작성하기</button>
            </div>
        </RootContainer>
    );
}

export default BoardWrite;