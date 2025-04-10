// import {useUser} from "../context/UserContext";
// import {useState} from "react";
// import {useNavigate} from "react-router-dom";
// import axios from "axios";
//
// function SignIn() {
//     const { state, setState } = useUser();
//     const [email, setEmail] = useState("");
//     const [password, setPassword] = useState("");
//     const navigate = useNavigate();
//
//
//
//     const signIn = async (e) => {
//         e.preventDefault();
//         if(!email || !password) {
//             alert('아이디와 비밀번호를 입력해 주세요')
//             return;
//         }
//         try {
//             // 로그인 요청
//             const response = await axios.post("/api/sign-in", {
//                 email,
//                 password
//             }, {
//                 withCredentials: true,
//                 headers: {
//                     'Content-Type': 'application/json'
//                 }
//             });
//
//             console.log("✅ 로그인 요청 성공:", response);



            // 이 부분이 중요합니다: 서버에서 사용자 정보를 응답으로 보내지 않는 경우
            // 상태를 업데이트하기 위해 사용자 정보를 가져오는 추가 요청을 해야 합니다
            //     try {
            //         const userResponse = await axios.get("/api/user", {
            //
            //             withCredentials: true
            //         });
            //
            //         if (userResponse.data) {
            //             console.log("✅ 사용자 정보:", userResponse.data);
            //             setState(userResponse.data); // UserContext 상태 업데이트
            //             alert("로그인 성공!");
            //             navigate("/");
            //         }
            //     } catch (userError) {
            //         console.error("사용자 정보 가져오기 실패:", userError.response || userError);
            //         // console.error("사용자 정보 가져오기 실패:", userError);
            //         alert("로그인은 되었지만 사용자 정보를 가져오는데 실패했습니다.");
            //     }
            // } catch (error) {
            //     console.error("로그인 에러: ", error);
            //     if (error.response && error.response.data.error) {
            //         alert(error.response.data.error);
            //     } else {
            //         alert("로그인 실패! 다시 시도해주세요.");
            //     }
            // }
        // };

//
//     try {
//         const response = await axios.post("/api/sign-in", {
//             email,
//             password
//         }, {
//             withCredentials: true,
//             headers: {
//                 'Content-Type': 'application/json'
//             }
//         });
//         console.log("✅ 서버 응답:", response.data);
//         if (response.data.user) {
//             alert(response.data.message);
//             console.log("setState 실행 전, 현재 state:", state);
//             setState(response.data.user);//로그인 성공후 바로 상태값 변경
//             console.log("setState 실행 후, 현재 state:", state);
//             sessionStorage.setItem("user", JSON.stringify(response.data.user));
//             console.log("✅ 세션에 저장된 값:", sessionStorage.getItem("user"));
//         }
//         navigate("/");//리엑트 라우터 방식 상태를 유지한다
//         // window.location.href='/'
//
//     } catch (error) {
//         console.error("로그인 에러: ", error);
//         if (error.response && error.response.data.error) {
//             alert(error.response.data.error);  // 서버에서 보낸 에러 메시지 설정
//         } else {
//             alert("로그인 실패! 다시 시도해주세요.");
//         }
//     }
// };

//토큰
// import axios from "axios";
// import {getUserFromToken, setTokens} from "../utils/jwt";
//
// const signIn = async (e) => {
//     e.preventDefault();
//     if(!email || !password) {
//         alert('아이디와 비밀번호를 입력해 주세요')
//         return;
//     }
//     try {
//         // 로그인 요청
//         const response = await axios.post("/api/sign-in", { email, password }, );
//         console.log("✅ 로그인 요청 성공");
//
//         const { accessToken, refreshToken } = response.data;
//
//         // 🔹 유틸 함수로 토큰 저장
//         setTokens(accessToken, refreshToken);
//
//         // 🔹 유저 정보 가져와서 상태 업데이트
//         const user = getUserFromToken();
//         if (user) {
//             setState(user);
//             console.log("✅ 로그인한 유저 정보:", user);
//             alert("로그인 성공")
//             navigate("/"); // 🔹 로그인 성공 시 메인 페이지로 이동
//         } else {
//             alert("로그인 정보가 유효하지 않습니다.");
//         }
//
//     } catch (error) {
//         console.error("로그인 에러: ", error);
//
//         if (error.response) {
//             alert(error.response.data.error);
//         }
//         else {
//             alert("로그인 요청 에러")
//         }
//     }
// };