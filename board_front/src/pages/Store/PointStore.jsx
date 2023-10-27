import React from 'react';
import RootContainer from '../../components/RootContainer/RootContainer';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { useQuery, useQueryClient } from 'react-query';
import { instance } from '../../api/config/instance';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

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

function PointStore(props) {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

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

    // iamport api 사용
    useEffect(() => {
        const iamport = document.createElement("script");
        iamport.src = "https://cdn.iamport.kr/v1/iamport.js"; // SDK Library 로드하기
        document.head.appendChild(iamport); // 페이지 마운트 될때 추가
        return () => {
            document.head.removeChild(iamport); // 페이지 언마운트 될때 삭제
        }
    }, []);

    const handlePaymentSubmit = (product) => {
        const principal = queryClient.getQueryState("getPrincipal");
        if(!window.IMP) {
            return;
        }
        const { IMP } = window;
        IMP.init("imp55744845");

        const paymentData = {
            pg: "kakaopay",
            pay_method: "kakaopay",
            merchant_uid: `mid_${new Date().getTime()}`,
            amount: product.productPrice,
            name: product.productName,
            buyer_name: principal?.data?.data?.name,
            buyer_email: principal?.data?.data?.email
        }

        IMP.request_pay(paymentData, (response) => {
            const { success, error_msg } = response;

            if(success) {
                const orderData = {
                    productId: product.productId,
                    email: principal?.data?.data?.email
                }
                const option = {
                    headers: {
                        Authorization: localStorage.getItem("accessToken")
                    }
                }
                instance.post("/order", orderData, option).then((response) => {
                    alert("포인트 충전이 완료되었습니다.");
                    queryClient.refetchQueries(["getProducts"]);
                    navigate("/account/mypage");
                });
            } else {
                alert(error_msg);
            }
        });
    }

    return (
        <RootContainer>
            <h1>포인트 충전하기</h1>
            <div css={SStoreContainer}>
                {!getProducts.isLoading && getProducts?.data?.data.map(product => {
                    return <button key={product.productId} onClick={() => {handlePaymentSubmit(product);}}>{product.productName} Point</button>
                })}
            </div>
        </RootContainer>
    );
}

export default PointStore;