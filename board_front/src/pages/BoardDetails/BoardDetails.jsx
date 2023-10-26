import React from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import { useQuery, useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';
import { useParams } from 'react-router-dom';
import { useState } from 'react';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';

const BoardContainer = css`
    position: relative;
    width: 928px;

    & h1 {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        font-size: 40px;
        word-wrap: break-word; // 다음 줄로 넘어가고 필요한 경우에 단어의 줄바꿈도 동시에 일어남
    }
`;

const WriterBox = css`
    display: flex;
    justify-content: flex-end;
    font-size: 12px;
`;

const SSideOption = css`
    position: absolute;
    display: flex;
    flex-direction: column;
    right: -80px;
    height: 100%;
`;

const SLikeButton = (isLike) => css`
    position: sticky;
    top: 150px;
    border: none;
    border-radius: 50px;
    width: 50px;
    height: 50px;
    background-color: ${isLike ? "#FFDEE9" : "transparent"};
    cursor: pointer;

    & div {
        margin-top: 5px;
    }
`;

const SCheckButton = css`
    position: sticky;
    top: 200px;
    border: none;
    border-radius: 50px;
    width: 50px;
    height: 50px;
    background-color: transparent;
    cursor: pointer;

    & div {
        margin-top: 5px;
    }
`;

const Line = css`
    width: 100%;
    margin: 30px 0px;
    border-bottom: 2px solid #dbdbdb;
`;

const ContentContainer = css`
    width: 100%;
    word-wrap: break-word;

    & * {
        word-wrap: break-word;
    }

    & img {
        max-width: 100%;
    }
`;

function BoardDetails(props) {

    const queryClient = useQueryClient();
    const principal = queryClient.getQueryState("getPrincipal");

    const { boardId } = useParams();
    const [ board, setBoard ] = useState({});
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

    const getLikeState = useQuery(["getLikeState", boardId], async () => {
        try {
            const option = {
                headers: {
                    Authorization: localStorage.getItem("accessToken")
                }
            }
            return await instance.get(`/board/like/${boardId}`, option);
        }catch(error) {
            console.error(error);
        }
    }, {
        refetchOnWindowFocus: false,
        retry: 0
    });
    
    if(getBoard.isLoading) {
        return <></>
    }

    const handleLikeButtonClick = async () => {
        const option = {
            headers: {
                Authorization: localStorage.getItem("accessToken")
            }
        }
        try{
            if(!!getLikeState?.data?.data) {
                await instance.delete(`/board/like/${boardId}`, option);
            }else {
                await instance.post(`/board/like/${boardId}`, {}, option);
            }
            getLikeState.refetch();
        }catch(error) {
            console.error(error);
        }
    }
    
    console.log(board);

    return (
        <RootContainer>
            <div css={BoardContainer}>
                <h1>{board.boardTitle}</h1>
                <div css={WriterBox}>
                    <p><b>{board.nickname}</b> - {board.createDate}</p>
                    <div css={SSideOption}>
                        {!getLikeState.isLoading &&
                        <button css={SLikeButton(getLikeState?.data?.data)} disabled={!principal?.data?.data} onClick={handleLikeButtonClick}>
                            <div>❤️</div>
                            <div>10</div>
                        </button>}
                        <button css={SCheckButton} disabled={!principal?.data?.data}>
                            <div>👀</div>
                            <div>200</div>
                        </button>
                    </div>
                </div>
                <div css={Line}></div>
                <div css={ContentContainer} dangerouslySetInnerHTML={{__html: board.boardContent}}></div>
            </div>
        </RootContainer>
    );
}

export default BoardDetails;