import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { instance } from '../../api/config/instance';

const layout = css`
    display: flex;
    flex-direction: column;
    align-items: center;

    & > input {
        margin-bottom: 5px;
    }
`;

const buttonBox = css`
    & > button {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 5px;
        width: 173px;
    }
`;

function Signin(props) {
    const navigate = useNavigate();

    const user = {
        email: "",
        password: ""
    }

    const [ signinUser, setSigninUser ] = useState(user);

    const handleInputChange = (e) => {
        setSigninUser({
            ...signinUser,
            [e.target.name]: e.target.value
        });
    }

    const handlesignupOnClick = () => {
        navigate("/auth/signup");
    }

    const handleSigninSubmit = async () => {
        try {
            const response = await instance.post("/auth/signin", signinUser); // 만들어진 토큰을 가져옴
            alert("로그인 성공 !");
            localStorage.setItem("accessToken", "Bearer " + response.data) // localStorage에 토큰 저장
            window.location.replace("/");
        }catch(error) {
            if(error.response.status === 401) {
                alert(error.response.data.authError);
            }
        }
    }

    const handleKakaoLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/kakao";
    }

    const handleNaverLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/naver";
    }

    return (
        <div css={layout}>
            <input type="email" name='email' onChange={handleInputChange} placeholder='이메일'/>
            <input type="password" name='password' onChange={handleInputChange} placeholder='비밀번호'/>
            <div css={buttonBox}>
                <button onClick={handleSigninSubmit}>로그인</button>
                <button onClick={handlesignupOnClick}>회원가입</button>
                <button onClick={handleKakaoLogin}>카카오 로그인</button>
                <button onClick={handleNaverLogin}>네이버 로그인</button>
            </div>
        </div>
    );
}

export default Signin;