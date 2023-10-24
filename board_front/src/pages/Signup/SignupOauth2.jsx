import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { instance } from '../../api/config/instance';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { useEffect } from 'react';

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
        margin-right: 1.5px;
        width: 85px;
    }
`;

function SignupOauth2(props) {
    const [ searchParams, setSearchParams ] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        if(!window.confirm("등록되지 않은 간편로그인 사용자입니다. 회원등록 하시겠습니까?")) {
            window.location.replace("/auth/signin");
        }
    }, [])
    
    const user = {
        email: "",
        password: "",
        name: searchParams.get("name"),
        nickname: "",
        oauth2Id: searchParams.get("oauth2Id"),
        profileImg: searchParams.get("profileImg"),
        provider: searchParams.get("provider")
    }

    console.log(user);

    const [ signupUser, setSignupUser ] = useState(user);

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
            alert("회원가입 성공 !");
            navigate("/auth/signin");
        }catch(error) {
            console.error(error);
            if(Object.keys(error.response.data).includes("email")) {
                // 계정 통합 권유
                if(window.confirm(`해당 이메일로 가입된 계정이 있습니다. \n${signupUser.provider} 계정과 연결하시겠습니까?`)) {
                    navigate(`/auth/oauth2/signup/merge?oauth2Id=${signupUser.oauth2Id}&email=${signupUser.email}&provider=${signupUser.provider}`);
                }
            }
        }
    }

    return (
        <div css={layout}>
            <input type="email" name='email' onChange={handleInputChange} placeholder='이메일'/>
            <input type="password" name='password' onChange={handleInputChange} placeholder='비밀번호'/>
            <input type="text" name='name' value={signupUser.name} onChange={handleInputChange} placeholder='이름' disabled={true}/>
            <input type="text" name='nickname' onChange={handleInputChange} placeholder='닉네임'/>
            <div css={buttonBox}>
                <button onClick={handleSignupSubmit}>가입하기</button>
                <button onClick={handleSigninClick}>로그인</button>
            </div>
        </div>
    );
}

export default SignupOauth2;