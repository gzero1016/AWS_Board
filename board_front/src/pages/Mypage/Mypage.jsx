import React, { useRef, useState } from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import { useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { ref, getDownloadURL, uploadBytes } from "firebase/storage";
import { storage } from "firebase/storage";

const infoHeader = css`
    display: flex;
    align-items: center;
    margin: 10px;
    border: 1px solid #dbdbdb;
    border-radius: 10px;
    padding: 20px;
    width: 100%;
`;

const imgBox = css`
    margin-right: 20px;
    border: 1px solid #dbdbdb;
    border-radius: 50%;
    width: 100px;
    height: 100px;
    overflow: hidden;
    cursor: pointer;
`;

const textBox = css`
    margin: 0px 14px;
`;

const file = css`
    display: none;
`;

function Mypage(props) {
    const queryClient = useQueryClient();
    const principalState = queryClient.getQueryState("getPrincipal");
    const principal = principalState.data.data;
    const profileFileRef = useRef();
    const [ uploadFiles, setUploadFiles ] = useState([]);

    const handleProfileUploadClick = () => {
        if(window.confirm("프로필 사진을 변경하시겠습니까?")) {
            profileFileRef.current.click();
        }
    }

    const handleProfileChange = (e) => {
        const files = e.target.files;

        if(files.length === 0) {
            return;
        }

        for(let file of files) {
            setUploadFiles([
                ...uploadFiles, file
            ]);
        }
    }

    const handleSendMail = async () => {
        try {
            const option = {
                headers: {
                    Authorization: localStorage.getItem("accessToken")
                }
            }
            await instance.post("/account/mail/auth", {}, option);
            alert("인증메일 전송 완료. 인증 요청 메일을 확인해주세요.");
        }catch(error) {
            console.error(error);
            alert("인증메일 전송 실패. 다시 시도해주세요.");
        }
    };

    return (
        <RootContainer>
            <div>
                <div css={infoHeader}>
                    <div>
                        <div css={imgBox} onClick={handleProfileUploadClick}>
                            <img src="" alt="" />
                        </div>
                        <input css={file} type="file" onChange={handleProfileChange} ref={profileFileRef}/>
                        {!uploadFiles && 
                            <div>
                                <button></button>
                                <button></button>
                            </div>
                        }
                    </div>
                    <div>
                        누적 포인트: 0원
                    </div>
                </div>
                <div css={textBox}>
                    <div>닉네임: {principal.nickname} </div>
                    <div>이름: {principal.name} </div>
                    <div>
                        이메일: {principal.email} {principal.enabled 
                        ? <button disabled={true}>인증완료</button> 
                        : <button onClick={handleSendMail}>인증하기</button>}
                    </div>
                </div>
            </div>
        </RootContainer>
    );
}

export default Mypage;