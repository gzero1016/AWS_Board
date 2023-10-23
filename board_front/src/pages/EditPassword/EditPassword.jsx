import React, { useState } from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import { instance } from '../../api/config/instance';
import { useNavigate } from 'react-router-dom';

function EditPassword(props) {
    const navigate = useNavigate();
    const [ passwordObj, setPasswordObj ] = useState({
        oldPassword: "",
        newPassword: "",
        checkNewPassword: ""
    });

    const handleInputChange = (e) => {
        setPasswordObj({
            ...passwordObj,
            [e.target.name]: e.target.value
        });
    }

    const handleUpdatePasswordSubmit = async () => {
        try{
            const option = {
                headers: {
                    Authorization: localStorage.getItem("accessToken")
                }
            }
            await instance.put("/account/password", passwordObj, option);
            alert("비밀번호 변경완료");
            navigate("/account/mypage");
        }catch(error){
            console.error(error)
            if(error.response.data.mismatched){
                alert(error.response.data.mismatched);
            }else if(error.response.data.authError){
                alert(error.response.data.authError);
            }
        }
    }

    return (
        <RootContainer>
            <div>
                <div><input type="password" name='oldPassword' 
                    onChange={handleInputChange} placeholder='이전 비밀번호' /></div>
                <div>
                    <input type="password" name='newPassword' 
                    onChange={handleInputChange} placeholder='새 비밀번호' />
                </div>
                <div>
                    <input type="password" name='checkNewPassword' 
                    onChange={handleInputChange} placeholder='새 비밀번호 확인' />
                </div>
                <button onClick={handleUpdatePasswordSubmit}>변경하기</button>
            </div>
        </RootContainer>
    );
}

export default EditPassword;