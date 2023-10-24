import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Signin from '../../pages/Signin/Signin';
import Signup from '../../pages/Signup/Signup';
import { useQueryClient } from 'react-query';
import SignupOauth2Merge from '../../pages/Signup/SignupOauth2Merge';
import SigninOauth2 from '../../pages/Signin/SigninOauth2';
import SignupOauth2 from '../../pages/Signup/SignupOauth2';

function AuthRoute(props) {

    const queryClient = useQueryClient();
    const principalState = queryClient.getQueryState("getPrincipal");

    // ?. 는 객체가 있어야 참조하겠다.
    if (!!principalState?.data?.data) {
        return <Navigate to={"/"} />
    }

    return (
        <Routes>
            <Route path='signin' element={ <Signin /> } />
            <Route path='oauth2/login' element={ <SigninOauth2 /> } />
            <Route path='signup' element={ <Signup /> } />
            <Route path='/oauth2/signup' element={ <SignupOauth2 /> } />
            <Route path='/oauth2/signup/merge' element={ <SignupOauth2Merge /> } />
        </Routes>
    );
}

export default AuthRoute;