import React from 'react';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { Link, useNavigate } from 'react-router-dom';
import { useQueryClient } from 'react-query';

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
`;

function SideBar(props) {
    const navigate = useNavigate();

    const queryClient = useQueryClient();

    const principalState = queryClient.getQueryState("getPrincipal")

    const signinOnClick = () => {
        navigate("/auth/signin");
    }

    const LogoutOnClick = () => {
        localStorage.removeItem("accessToken");
        window.location.replace("/")
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
                        <Link to={"/auth/forgot/password"}>비밀번호 찾기</Link>
                        <Link to={"/auth/signup"}>회원가입 찾기</Link>
                    </div>
                </div>
            )}
        </div>
    );
}

export default SideBar;