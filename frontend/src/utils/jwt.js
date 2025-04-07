//토큰 저장
export function setTokens(accessToken, refreshToken) {
    sessionStorage.setItem("accessToken", accessToken);
    sessionStorage.setItem("refreshToken", refreshToken);
}

//액세스 토큰 호출
export function getAccessToken() {
    return sessionStorage.getItem("accessToken");
}

//리프레시 토큰 호출
export function getRefreshToken() {
    return sessionStorage.getItem("refreshToken");
}

//로그아웃 토큰 삭제
export function clearTokens() {
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("refreshToken");
}

export function getUserFromToken() {
    const token = getAccessToken();
    if (!token) return null;

    try {
        // 직접 토큰 디코딩
        const base64Url = token.split(".")[1];
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split("")
                .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                .join("")
        );

        const decoded = JSON.parse(jsonPayload);

        if (decoded && decoded.exp * 1000 > Date.now()) {
            return {
                email: decoded.sub,
                username: decoded.username,
                nickname: decoded.nickname,
                roleName: decoded.roleName
            };
        } else {
            clearTokens(); // 만료된 토큰 삭제
            return null;
        }
    } catch (error) {
        console.error("토큰 디코딩 실패:", error);
        clearTokens();
        return null;
    }
}