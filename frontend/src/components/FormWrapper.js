import React from 'react';

export default function FormWrapper({ onSubmit, children, onReset, submitText = "제출", resetText = "초기화" }) {
    return (
        <form onSubmit={onSubmit}>
            {children}
            <button type="submit" className="btn btn-primary">{submitText}</button>
            {onReset && <button type="button" className="btn btn-secondary" onClick={onReset}>{resetText}</button>}
        </form>
    );
}