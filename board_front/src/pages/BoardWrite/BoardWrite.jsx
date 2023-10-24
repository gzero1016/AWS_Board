import React, { useEffect, useState } from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import ReactQuill from 'react-quill';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import Select from 'react-select';
import { instance } from '../../api/config/instance';

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
    }
`;

const buttonContainer = css`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 60px;
    width: 100%;
`;

function BoardWrite(props) {
    const [ newCategory, setNewCategory ] = useState("");
    const [ selectOptions, setSelectOptions ]= useState("");
    const [ selectedOption, setSelectedOption ] = useState(selectOptions[0]);

    // 해당 주소로 정상적으로 다녀왔다면~!
    // response의 data를 맵을 돌려서 category안에있는 name을 들고옴
    useEffect(() => {
        instance.get("/board/categories").then((response) => {
            setSelectOptions(
                response.data.map(category => {
                    return{ value: category.boardCategoryName, label: category.boardCategoryName }
                })
            )
        })
    }, [])

    useEffect(() => {
        if(!!newCategory){
            const newOption = { value: newCategory, label: newCategory }

            setSelectedOption(newOption);
            if(!selectOptions.map(option => option.value).includes(newOption.value)) {
                setSelectOptions([
                ...selectOptions,
                    {value: newCategory, label: newCategory}
                ]);
            }
        }
    }, [newCategory]);

    const modules = {
        toolbar: {
            container: [
                [{header: [1, 2, 3, false]}],
                ["bold", "underline"],
                ["image"]
            ]
        }
    }

    const handleTitleInput = () => {

    }

    const handleContentInput = (value) => {
        console.log(value);
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

    return (
        <RootContainer>
            <div>
                <h1>글쓰기</h1>
                <div css={categoryContainer}>
                    <div css={selectBox}>
                        <Select options={selectOptions} onChange={handleSelectChange} defaultValue={selectedOption} />
                    </div>
                    <button onClick={handleCategoryAdd}>카테고리 추가</button>
                </div>
                <div><input css={titleInput} type="text" name='title' placeholder='제목' onChange={handleTitleInput} /></div>
                <div>
                    <ReactQuill style={{width: "928px", height: "510px"}} modules={modules} onChange={handleContentInput} />
                </div>
            </div>
            <div css={buttonContainer}>
                <button>작성하기</button>
            </div>
        </RootContainer>
    );
}

export default BoardWrite;