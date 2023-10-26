import React, { useEffect, useState } from 'react';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { Link, useNavigate } from 'react-router-dom';
import { useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';

const layout = css`
    margin-right: 10px;
    width: 320px;
`;

const container = css`
    display: flex;
    flex-direction: column;
    align-items: center;
    border: 1px solid #dbdbdb;
    border-radius: 10px;
    padding: 20px;

    & button {
        margin-bottom: 5px;
    }
`;

function SideBar(props) {
    const [ categories, setCategories ] = useState([]);
    const navigate = useNavigate();
    const queryClient = useQueryClient(); // queryClient 객체를 가져옴
    const principalState = queryClient.getQueryState("getPrincipal") 

    useEffect(() => {
        instance.get("/board/categories").then((response) => {
                    setCategories(response.data)
                }
            )
    }, [])

    const signinOnClick = () => {
        navigate("/auth/signin");
    }

    const LogoutOnClick = () => {
        localStorage.removeItem("accessToken");
        window.location.replace("/") // replace: 완전 재시작 , 상태 다 날라감
        // navigate: 페이지 이동은 되지만 , 상태가 유지됨 (useQuery가 재실행안됨)
    }

    return (
        <div css={layout}>
            {!!principalState?.data?.data ? ( 
                <div css={container}>
                    <h3>{principalState.data.data.nickname}님 환영합니다.</h3>
                    <div><button onClick={LogoutOnClick}>로그아웃</button></div>
                    <div>
                        <Link to={"/account/mypage"}>마이페이지</Link>
                    </div>
                </div>
            ) : ( 
                <div css={container}>
                    <h3>로그인 후 게시판을 이용해보세요</h3>
                    <div><button onClick={signinOnClick}>로그인</button></div>
                    <div>
                        <Link to={"/auth/forgot/password"}>비밀번호 찾기 | </Link>
                        <Link to={"/auth/signup"}>회원가입</Link>
                    </div>
                </div>
            )}
            <div>
                <ul>
                    <Link to={"/board/write"}><li>글쓰기</li></Link>
                    {/* 모든 categoryCount를 더하는거 */}
                    <Link to={"/board/all/1"}><li>전체 게시글 ({categories.map(category => 
                        category.boardCount).reduce((sum, curValue) => sum + curValue, 0)})</li></Link>
                    {categories.map(category => { {/* DB에 있는 categoryName을 사이드바에 펼치기 */}
                        return <Link 
                                    key={category.boardCategoryId} 
                                    to={`/board/${category.boardCategoryName}/1`}
                                    reloadDocument={true}>
                            <li>{category.boardCategoryName} ({category.boardCount})</li>
                        </Link>
                    })}
                </ul>
            </div>

        </div>
    );
}

export default SideBar;