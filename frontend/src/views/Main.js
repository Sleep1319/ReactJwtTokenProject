import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { getAccessToken } from "../utils/jwt";

function Main() {
    const [posts, setPosts] = useState([]);
    const [totalPages, setTotalPages] = useState(0);  // 총 페이지
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [searchTitle, setSearchTitle] = useState(""); // 제목 검색
    const [searchNickname, setSearchNickname] = useState(""); // 작성자 검색
    const navigate = useNavigate();
    const token = getAccessToken();

    useEffect(() => {
        console.log("호출!")
        const getBoard = async () => {
            try {
                const response = await axios.get(`/api/boards?page=${currentPage}&title=${searchTitle}&nickname=${searchNickname}`);
                setPosts(response.data.content);
                console.log(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error) {
                console.error("Error fetching posts:", error);
            }
        };
    
        getBoard();
    }, [currentPage,  searchTitle, searchNickname]);//이 값이 변경되면 감지한다

    const selectBoard = (id) => {
        navigate(`/board/${id}`);
    };

    //페이징 변경 감지 => 감지하여 상태가 변하면 바뀐 값으로 메소드 호출
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };
    // 제목 검색 입력
    const handleSearchTitleChange = (e) => {
        setSearchTitle(e.target.value);
        setCurrentPage(0); // 검색 시 첫 페이지로 돌아가기
    };

    // 닉네임 검색 입력
    const handleSearchNicknameChange = (e) => {
        setSearchNickname(e.target.value);
        setCurrentPage(0); // 검색 시 첫 페이지로 돌아가기
    };

    const pageSize = 3; // 한 번에 보여줄 페이지 버튼 수
    const startPage = Math.floor(currentPage / pageSize) * pageSize;
    const endPage = Math.min(startPage + pageSize, totalPages)
    const pagesToShow = [];

    for (let i = startPage; i < endPage; i++) {
        pagesToShow.push(i);
    }

    return (
        <main className="main">
            <div>
                {/* 검색 입력 폼 */}
                <input
                    type="text"
                    placeholder="제목 검색"
                    value={searchTitle}
                    onChange={handleSearchTitleChange}
                />
                <input
                    type="text"
                    placeholder="닉네임 검색"
                    value={searchNickname}
                    onChange={handleSearchNicknameChange}
                />
            </div>
            {posts.length > 0 ? (
            <div>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <td>게시글 번호</td>
                            <td>제목</td>
                            <td>작성자</td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        {posts.map((post) => (
                                <tr key={post.id} onClick={() => selectBoard(post.id)} style={{ cursor: "pointer" }}>
                                    <td>{post.id}</td>
                                    <td>{post.title}</td>
                                    <td>{post.nickname}</td>
                                </tr>
                        ))}
                    </tbody>
                </table>
                <div style={{ marginTop: "20px" }}>
                    {/* 이전 버튼 */}
                    {startPage > 0 && (
                        <button
                            onClick={() => handlePageChange(startPage - 1)}
                            style={{
                                margin: "0 5px",
                                padding: "5px 10px",
                                cursor: "pointer",
                                backgroundColor: "#6c757d",
                                color: "#fff",
                                border: "none",
                                borderRadius: "3px"
                            }}
                        >
                            이전
                        </button>
                    )}

                    {/* 페이지 번호 버튼 */}
                    {pagesToShow.map((pageNum) => (
                        <button
                            key={pageNum}
                            onClick={() => handlePageChange(pageNum)}
                            disabled={currentPage === pageNum}
                            style={{
                                margin: "0 5px",
                                padding: "5px 10px",
                                cursor: "pointer",
                                backgroundColor: currentPage === pageNum ? "gray" : "#007bff",
                                color: "#fff",
                                border: "none",
                                borderRadius: "3px"
                            }}
                        >
                            {pageNum + 1}
                        </button>
                    ))}

                    {/* 다음 버튼 */}
                    {endPage < totalPages && (
                        <button
                            onClick={() => handlePageChange(endPage)}
                            style={{
                                margin: "0 5px",
                                padding: "5px 10px",
                                cursor: "pointer",
                                backgroundColor: "#6c757d",
                                color: "#fff",
                                border: "none",
                                borderRadius: "3px"
                            }}
                        >
                            다음
                        </button>
                    )}
                </div>
            </div>
            ) : (
                <div>
                게시글이 없습니다다
            </div>
            )}
            <Link to="/write-board">
            <input type="checkbox" className="btn-check" id="btn-check" autoComplete="off"/>
            <label className="btn btn-primary" for="btn-check">글쓰기</label>
            </Link>
        </main>
    );
}

export default Main;