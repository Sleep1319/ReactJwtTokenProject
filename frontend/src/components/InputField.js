import React from 'react';

export default function InputField({ label, type, value, onChange, id }) {
    return (
        <div className="mb-3">
            <label htmlFor={id} className="form-label">{label}</label>
            <input
                type={type}
                className="form-control"
                id={id}
                value={value}
                onChange={onChange}
            />
        </div>
    );
}