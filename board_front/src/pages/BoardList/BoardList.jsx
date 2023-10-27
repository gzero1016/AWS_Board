import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import RootContainer from '../../components/RootContainer/RootContainer';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import ReactSelect from 'react-select';
import { useState } from 'react';
import { instance } from '../../api/config/instance';
import { QueryClient, useQuery, useQueryClient } from 'react-query';

const table = css`
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #dbdbdb;

    & th, td {
        border: 1px solid #dbdbdb;
        height: 30px;
        text-align: center;
    }

    & td {
        cursor: pointer;
    }
`;

const searchContainer = css`
    display: flex;
    justify-content: flex-end;
    margin-bottom: 10px;
    width: 100%;

    & > * {
        margin-left: 5px;
    }
`;

const selectBox = css`
    width: 110px;
`;

const SPageNumbers = css`
    display: flex;
    align-items: center;
    margin-top: 10px;
    width: 200px;

    & button {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 3px;
        width: 20px;
        border: 1px solid #dbdbdb;
        cursor: pointer;
    }
`;

const pageNumber = css`
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
`;

const SBoardTitle = css`
    max-width: 400px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-weight: 600;
`;

function BoardList(props) {
    const navigate = useNavigate();
    const { category, page } = useParams();

    const options = [
        {value: "전체", label: "전체"},
        {value: "제목", label: "제목"},
        {value: "작성자", label: "작성자"}
    ];

    const search = {
        optionName: options[0].label,
        searchValue: ""
    };

    const [ searchParams, setSearchParams ] = useState(search);

    const getBoardList = useQuery(["getBoardList", page, category], async () => {
        const option = {
            params: searchParams
        }
        return await instance.get(`/boards/${category}/${page}`, option)
    }, {
        refetchOnWindowFocus: false
    });

    const getBoardCount = useQuery(["getBoardCount", page, category], async () => {
        const option = {
            params: searchParams
        }
        return await instance.get(`/boards/${category}/count`, option);
    }, {
        refetchOnWindowFocus: false
    });

    const handleSearchInputChange = (e) => {
        setSearchParams({
            ...searchParams,
            searchValue: e.target.value
        })
    };

    const handleSearchOptionSelect = (option) => {
        setSearchParams({
            ...searchParams,
            optionName: option.label
        })
    };

    const handleSearchButtonClick = () => {
        navigate(`/board/${category}/1`);
        getBoardList.refetch();
        getBoardCount.refetch();
    };

    // getBoardCount.isLoading가 false면 getBoardCount.data.data 이걸실행한다.
    // console.log(!getBoardCount.isLoading && getBoardCount.data.data);s

    const pagination = () => {
        if(getBoardCount.isLoading) {
            return <></>
        }

        const totalBoardCount = getBoardCount.data.data;

        // 몊번째 페이지까지 나오는지
        const lastPage = totalBoardCount % 10 === 0 ? totalBoardCount / 10 : Math.floor(totalBoardCount / 10) + 1;

        const startIndex = parseInt(page) % 5 === 0 ? parseInt(page) - 4 : parseInt(page) - (parseInt(page) % 5) + 1;
        
        const endIndex = startIndex + 4 <= lastPage? startIndex + 4 : lastPage;
        
        const pageNumbers = [];

        for(let i = startIndex; i <= endIndex; i++) {
            pageNumbers.push(i);
        }

        return (
            <>
                <button disabled={parseInt(page) === 1} onClick={() => {
                    navigate(`/board/${category}/${parseInt(page) - 1}`);
                }}>&#60;</button>
                {pageNumbers.map(pageNumber =>{
                    return <button key={pageNumber} onClick={() => {
                        navigate(`/board/${category}/${pageNumber}`);
                    }}>{pageNumber}</button>
                })}
                <button disabled={parseInt(page) === lastPage} onClick={() => {
                    navigate(`/board/${category}/${parseInt(page) + 1}`);
                }}>&#62;</button>
            </>
        )
    }

    return (
        <RootContainer>
            <div>
                <h1>{category === "all" ? "전체 게시글" : category}</h1>
                <div css={searchContainer}>
                    <div css={selectBox}>
                        <ReactSelect options={options} defaultValue={options[0]} onChange={handleSearchOptionSelect} />
                    </div>
                    <input type="text" placeholder='search...' onChange={handleSearchInputChange} />
                    <button onClick={handleSearchButtonClick}>검색</button>
                </div>
                <table css={table}>
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>추천</th>
                            <th>조회수</th>
                        </tr>
                    </thead>
                    <tbody>
                        {!getBoardList.isLoading && getBoardList?.data.data.map(board => {
                            return  <tr key={board.boardId} onClick={() => {navigate(`/board/${board.boardId}`)}}>
                                        <td>{board.boardId}</td>
                                        <td css={SBoardTitle}>{board.title}</td>
                                        <td>{board.nickname}</td>
                                        <td>{board.createDate}</td>
                                        <td>{board.likeCount}</td>
                                        <td>{board.hitsCount}</td>
                                    </tr>
                            
                        })}
                    </tbody>
                </table>
                <div css={pageNumber}>
                    <div css={SPageNumbers}>
                        {pagination()}
                    </div>
                </div>
            </div>
        </RootContainer>
    );
}

export default BoardList;