import React from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { useQuery } from 'react-query';
import { instance } from '../../api/config/instance';
import { useEffect } from 'react';

const SStoreContainer = css`
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    border: 1px solid #dbdbdb;
    border-radius: 10px;
    padding: 20px;
    width: 100%;

    & button {
        margin: 10px 0px;
        width: 30%;
        height: 120px;
        background-color: #eee;
        border: none;
        border-radius: 7px;
        cursor: pointer;

        &:hover {
            background-color: #fff;
            border: 2px solid #eee;
        }
    }
`;

function Store(props) {
    const getProducts = useQuery(["getProducts"], async () => {
        try {
            const option = {
                headers: {
                    Authorization: localStorage.getItem("accessToken")
                }
            }
            return await instance.get("/products", option);
        }catch(error) {
            console.error(error);
        }
    });

    console.log(getProducts?.data?.data)

    // iamport api 사용
    useEffect(() => {
        const iamport = document.createElement("script");
        iamport.src = "https://cdn.iamport.kr/v1/iamport.js"; // SDK Library 로드하기
        document.head.appendChild(iamport); // 페이지 마운트 될때 추가
        return () => {
            document.head.removeChild(iamport); // 페이지 언마운트 될때 삭제
        }
    }, []);

    return (
        <RootContainer>
            <h1>포인트 충전하기</h1>
            <div css={SStoreContainer}>
                {!getProducts.isLoading && getProducts?.data?.data.map(product => {
                    return <button key={product.productId}>{product.productName} Point</button>
                })}
            </div>
        </RootContainer>
    );
}

export default Store;