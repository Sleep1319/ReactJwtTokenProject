import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";

//"" 값 설정이유 언디파인 대용 콘솔창 에러로그들..
function Board() {
    const { state } = useUser();
    const { id } = useParams();
    const [post, setPost] = useState({ title: "", nickname: "", content: "" });
    const [isReadOnly, setIsReadOnly] = useState(true);
    const [editedContent, setEditedContent] = useState(""); 
    const navigate = useNavigate();

    useEffect(() => {
        const getBoardById = async () => {
            try {
                const response = await axios.get(`/api/post/${id}`);
                setPost(response.data || { title: "", nickname: "", content: "" });
                console.log("서버 응답:", response.data);
            } catch (error) {
                console.error("불러오기 실패", error); 
            }
        };
    
        if (id) getBoardById();
    }, [id]);

    const updateBoard = async () => {
        if (!window.confirm("정말로 수정하시겠습니까??")) return;

        try {
            const response = await axios.put(`/api/update-board/${id}`, { content: editedContent });
            setPost((prevPost) => ({ ...prevPost, content: editedContent }));
            setIsReadOnly(true);
            alert(response.data.message);
        } catch (error) {
            console.error("수정 실패", error.response)
            alert("수정실패");
        }
    };

    const deleteBoard = async () => {
        if (!window.confirm("글을 삭제하시겠습니까?")) return;
        
        try {
            const response = await axios.delete(`/api/delete-board/${id}`);
            alert(response.data.message)
            navigate("/");
        } catch (error) {
            console.error("삭제 실패", error);
            alert("삭제실패");
        }
    }

    useEffect(() => {
        if (post.content) {
            setEditedContent(post.content);
        }
    }, [post]);
    


    const handleCancel = () => {
        if (!window.confirm("수정을 취소하겠습니까?")) return;
        
        setIsReadOnly(true);
        setEditedContent(post.content);
    };
    
    return (
        <main className="main">
            <form className="boardForm">
                <span className="d-flex gap-3">
                    <div className="mb-3" style={{ width: "45%" }}>
                        <label htmlFor="exampleFormControlInput1" className="form-label">제목</label>
                        <input type="text" className="form-control" id="exampleFormControlInput1" value={post.title || ""} readOnly/>
                    </div>
                    <div className="mb-3" style={{ width: "45%" }}>
                        <label htmlFor="exampleFormControlInput2" className="form-label">작성자</label>
                        <input type="text" className="form-control" id="exampleFormControlInput2" value={post.nickname || ""} readOnly/>
                    </div>
                </span>
                <div className="mb-3">
                    <label htmlFor="exampleFormControlTextarea1" className="form-label">내용</label>
                    <textarea className="form-control" id="content" rows="20" value={editedContent} readOnly={isReadOnly} onChange={(e) => setEditedContent(e.target.value)}></textarea>
                </div>
            </form>
            {post && post.member_id && Number(state?.userId) === Number(post.member_id) && (
                <>
                    {isReadOnly ? (
                        <button className="btn btn-warning" onClick={() => setIsReadOnly(false)}>수정하기</button>
                    ) : (
                        <>
                            <button className="btn btn-success" onClick={updateBoard}>확인</button>
                            <button className="btn btn-warning" onClick={handleCancel}>취소</button>
                        </>
                    )}
                </>
            )}
            {(post && post.member_id && (Number(state?.userId) === Number(post.member_id) || state.roleName === "ADMIN")) && (
                <button className="btn btn-danger" onClick={deleteBoard}>글 삭제</button>
            )}
            <Link to="/">메인으로 돌아가기</Link>
        </main>
    );
}
export default Board;