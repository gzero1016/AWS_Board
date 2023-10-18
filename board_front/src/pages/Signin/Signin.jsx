import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { instance } from '../../api/config/instance';

const layout = css`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

function Signin(props) {
    const navigate = useNavigate();

    const user = {
        email: "",
        password: ""
    }

    const [ signinUser, setSigninUser ] = useState();

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
            const response = await instance.post("/auth/signin", signinUser);
            alert("로그인 성공 !");
            localStorage.setItem("accessToken", "Bearer " + response.data)
            window.location.replace("/");
        }catch(error) {
            if(error.response.status === 401) {
                alert(error.response.data.authError);
            }
        }
    }

    return (
        <div css={layout}>
            <input type="email" name='email' onChange={handleInputChange} placeholder='이메일'/>
            <input type="password" name='password' onChange={handleInputChange} placeholder='비밀번호'/>
            <div>
                <button onClick={handleSigninSubmit}>로그인</button>
                <button onClick={handlesignupOnClick}>회원가입</button>
            </div>
        </div>
    );
}

export default Signin;