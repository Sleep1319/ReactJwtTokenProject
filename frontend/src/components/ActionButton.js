import React from "react";

export default function ActionButton({ onClick, type = "button", className = "btn btn-outline-primary", disabled = false, children}) {
  return (
    <button
      type={type}
      className={className}
      onClick={onClick}
      disabled={disabled}//조건에 따라 활성하 같은 식 추가시 이용
    >
      {children}
    </button>
  );
}