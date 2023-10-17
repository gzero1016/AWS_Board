import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { instance } from '../../api/config/instance';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';

const layout = css`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

function Signup(props) {
    const navigate = useNavigate();
    
    const user = {
        email: "",
        password: "",
        name: "",
        nickname: ""
    }

    const [ signupUser, setSignupUser ] = useState();

    const handleInputChange = (e) => {
        setSignupUser({
            ...signupUser,
            [e.target.name]: e.target.value
        });
    }

    const handleSigninClick = () => {
        navigate("/auth/signin");
    }

    const handleSignupSubmit = async () => {
        try {
            const response = await instance.post("/auth/signup", signupUser);
            console.log(response);
            alert("회원가입성공!");
        }catch(error) {
            console.error(error);
        }
    }

    return (
        <div css={layout}>
            <input type="email" name='email' onChange={handleInputChange} placeholder='이메일'/>
            <input type="password" name='password' onChange={handleInputChange} placeholder='비밀번호'/>
            <input type="text" name='name' onChange={handleInputChange} placeholder='이름'/>
            <input type="text" name='nickname' onChange={handleInputChange} placeholder='닉네임'/>
            <div>
                <button onClick={handleSignupSubmit}>가입하기</button>
                <button onClick={handleSigninClick}>로그인</button>
            </div>
        </div>
    );
}

export default Signup;