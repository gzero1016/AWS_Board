import React from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
import { useQueryClient } from 'react-query';

function Mypage(props) {
    const queryClient = useQueryClient();
    const principalState = queryClient.getQueryState("getPrincipal");
    const principal = principalState.data.data;

    return (
        <RootContainer>
            <div>
                <div>
                    <img src="" alt="" />
                </div>
                <div>
                    누적 포인트: 0원
                </div>
                <div>
                    <div>닉네임: {principal.nickname} </div>
                    <div>이름: {principal.name} </div>
                    <div>
                        이메일: {principal.email} {principal.enabled ? <button disabled={true}>인증완료</button> : <button>인증하기</button>}
                    </div>
                </div>
            </div>
        </RootContainer>
    );
}

export default Mypage;