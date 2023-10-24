import React from 'react';
import { useQueryClient } from 'react-query';
import { Navigate, useSearchParams } from 'react-router-dom';

function SigninOauth2(props) {
    const [ searchParams, setSearchParams ] = useSearchParams();
    const queryClient = useQueryClient();

    // 응답온 token을 localStorage 에 token을 집어넣은 후 다시 홈으로 돌아감
    // 스쳐지나가는 페이지
    localStorage.setItem("accessToken", "Bearer " + searchParams.get("token"));
     // getPrincipal로 사용자 정보를 들고와 로그인시켜줌
    queryClient.refetchQueries(["getPrincipal"]); // getPrincipal: 키값

    return <Navigate to={"/"} />;
}

export default SigninOauth2;