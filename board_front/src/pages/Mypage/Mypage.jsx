import React, { useEffect, useRef, useState } from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import { useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { ref, getDownloadURL, uploadBytes, uploadBytesResumable } from "firebase/storage";
import { storage } from "../../api/firebase/firebase";
import { Line } from 'rc-progress';

const infoHeader = css`
    display: flex;
    align-items: center;
    margin: 10px;
    border: 1px solid #dbdbdb;
    border-radius: 10px;
    padding: 20px;
    width: 97%;
`;

const imgBox = css`
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 20px;
    border: 1px solid #dbdbdb;
    border-radius: 50%;
    width: 100px;
    height: 100px;
    overflow: hidden;
    cursor: pointer;

    & > img {
        width: 100%;
    }
`;

const btBox = css`
    margin: 0px 0px 0px 10px;
    & button {
        margin-top: 5px;
        margin-right: 5px;
    }
`;

const textBox = css`
    margin: 0px 14px;

    & div {
        margin-top: 3px;
        font-weight: 600;
    }

    & button {
        margin-left: 5px;
    }
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
    const [ profileImgSrc, setProfileImgSrc ] = useState("");
    const [ progressPercent, setProgressPercent ] = useState(0);

    useEffect(() => {
        setProfileImgSrc(principal.profileUrl);
    }, [])

    const handleProfileUploadClick = () => {
        if(window.confirm("프로필 사진을 변경하시겠습니까?")) {
            profileFileRef.current.click();
        }
    }

    const handleProfileChange = (e) => {
        const files = e.target.files;
        // 파일선택 취소했을시
        if(!files.length) {
            setUploadFiles([]);
            e.target.value = "";
            return;
        }
        // 아래 for문은 파일을 여러개 올릴때
        for(let file of files) {
            setUploadFiles([
                ...uploadFiles, file
            ]);
        }
        // 프로필 사진은 하나만이니 for문 없이
        // setUploadFiles([...uploadFiles, files[0]]);

        const reader = new FileReader();
        reader.onload = (e) => {
            setProfileImgSrc(e.target.result);
        }
        reader.readAsDataURL(files[0])
    }

    const handleUploadSubmit = () => {
        const storageRef = ref(storage, `files/profile/${uploadFiles[0].name}`);
        const uploadTask = uploadBytesResumable(storageRef, uploadFiles[0]);

        uploadTask.on(
            "state_changed",
            (snapshot) => {
                setProgressPercent(
                    Math.round((snapshot.bytesTransferred / snapshot.totalBytes) * 100)
                )
            },
            (error) => {
                console.error(error)
            },
            () => {
                getDownloadURL(storageRef).then(downloadUrl => {
                    console.log(downloadUrl);
                    setProfileImgSrc(downloadUrl);
                    const option = {
                        headers: {
                            Authorization: localStorage.getItem("accessToken")
                        }
                    }
                    instance.put("/account/profile/img" , {profileUrl: downloadUrl}, option)
                    .then((response) => {
                        alert("프로필 사진이 변경되었습니다.");
                        window.location.reload();
                    });
                })
            }
        )
    }

    const handleUploadCancel = () => {
        setUploadFiles([]);
        profileFileRef.current.value = "";
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
                            <img src={profileImgSrc} alt="" />
                        </div>
                        <input css={file} type="file" onChange={handleProfileChange} ref={profileFileRef}/>
                        {!!uploadFiles.length && 
                            <div css={btBox}>
                                <Line percent={progressPercent} strokeWidth={3} strokeColor="#dbdbdb" />
                                <button onClick={handleUploadSubmit}>변경</button>
                                <button onClick={handleUploadCancel}>취소</button>
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