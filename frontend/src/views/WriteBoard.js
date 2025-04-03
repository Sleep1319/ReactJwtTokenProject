import axios from "axios";
import React, {useRef} from "react";
import { Link, useNavigate } from "react-router-dom"; 
import { useUser } from "../context/UserContext";

function WriteBoard() {
    const { state } = useUser();
    const title = useRef(); //한 번만 가져와서 서버로 보내고 이동하는 기능이기에 useState로 상태 처리 없이 사용
    const content = useRef();
    const memberId = state?.userId
    const navigate = useNavigate();

    const writeBoard = async (e) => {
        e.preventDefault();

        if(!memberId) {
            alert("유저 정보를 받아올 수 없습니다.");
            return;
        }

        if (!title || !content) {
            alert("모든 필드를 입력하세요.");
            return;
        }

        try {
            const response = await axios.post("/api/write-board", {
                title: title.current.value,
                content: content.current.value,
                memberId
            });
            alert(response.data.message);
            navigate("/");
        } catch (error) {
            console.error("게시글 등록오류", error);
            if (error.response && error.response.data.error) {
                alert(error.response.data.error); 
            } else {
                alert("게시글 등록 실패 다시 시도해주세요.");
            }
        }

    };


    
    return (
        <main className="main">
            <form id="writeForm" onSubmit={writeBoard}>
                <div className="mb-3">
                    <label htmlfor="exampleFormControlInput1" className="form-label">제목</label>
                    <input type="text" className="form-control" id="exampleFormControlInput1" ref={title}/>
                </div>
                <div className="mb-3">
                    <label htmlFor="exampleFormControlTextarea1" className="form-label">내용</label>
                    <textarea className="form-control" id="exampleFormControlTextarea1" ref={content} rows="20" maxLength={333} placeholder="333자 까지"></textarea>
                </div>
                <button type="submit" className="btn btn-primary" >등록</button>
            </form>
            <Link to="/">메인으로 돌아가기</Link>
        </main>
    );
}
export default WriteBoard;