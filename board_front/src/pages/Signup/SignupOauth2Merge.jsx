import React from 'react';
import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { instance } from '../../api/config/instance';

const layout = css`
    display: flex;
    flex-direction: column;
    align-items: center;

    & > button {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 5px;
        width: 173px;
    }
`;

function SignupOauth2Merge(props) {
    const [ searchParams, setSearchParams ] = useSearchParams();
    const [ mergeUser, setMergeUser ] = useState({
        email: searchParams.get("email"),
        password: "",
        oauth2Id: searchParams.get("oauth2Id"),
        provider: searchParams.get("provider")
    });

    const handleInputChange = (e) => {
        setMergeUser({
            ...mergeUser,
            [e.target.name]: e.target.value
        });
    }

    const handleMergeSubmit = async () => {
        try{
            await instance.put("/auth/oauth2/merge", mergeUser);
            alert("통합에 성공하였습니다.");
            window.location.replace("/auth/signin");
        }catch(error){
            alert(error.response.data.authError);
        }
    }

    return (
        <div css={layout}>
            <h1>{searchParams.get("email")} 계정과 {searchParams.get("provider")} 계정 통합</h1>
            <p>계정을 통합하시려면 가입된 사용자의 비밀번호를 입력하세요.</p>
            <div><input type='password' name='password' placeholder='비밀번호' onChange={handleInputChange}/></div>
            <button onClick={handleMergeSubmit}>확인</button>
        </div>
    );
}

export default SignupOauth2Merge;