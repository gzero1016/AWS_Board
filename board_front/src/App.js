import RootLayout from './components/RootLayout/RootLayout';
import './App.css';
import { Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import { useQuery } from 'react-query';
import { instance } from './api/config/instance';
import AuthRoute from './components/Routes/AuthRoute';
import AccountRoute from './components/Routes/AccountRoute';

function App() {

  // useQuery은 무조건 Get 요청
  const getPrincipal = useQuery(["getPrincipal"], async () => {
    try {
      const option = {
        headers: {
          Authorization: localStorage.getItem("accessToken")
        }
      }
      return await instance.get("/account/principal", option); // 리턴을 안걸어주면 데이터 안들어감

    } catch(error) {
      throw new Error(error)
    }
  }, {
    retry: 0, // 요청반복횟수
    refetchInterval: 1000 * 60 * 10, // 10분마다 토큰을 자동으로 요청보내서 유효하면 user정보를 가져오게함
    refetchOnWindowFocus: false // 포커스 움직일때마다 useQuery 랜더링 끄기
  });

  if(getPrincipal.isLoading){
    return <></>
  }
  
  return (
    <RootLayout>
      <Routes>
        <Route path='/' element={ <Home /> } />
        <Route path='/auth/*' element={ <AuthRoute /> } />
        <Route path='/account/*' element={ <AccountRoute /> } />
        <Route path='/board/:category' element={ <></> } />
        <Route path='/board/:category/register' element={<></>} />
        <Route path='/board/:category/edit' element={<></>} />
      </Routes>
    </RootLayout>
  );
}

export default App;
