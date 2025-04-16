import React, { forwardRef } from "react";

const TextareaField = forwardRef(({ label, id, value, onChange, readOnly = false, rows = 5 }, ref) => {
    return (
        <div className="mb-3">
            <label htmlFor={id} className="form-label">{label}</label>
            <textarea
                className="form-control"
                id={id}
                rows={rows}
                value={value}
                onChange={onChange}
                readOnly={readOnly}
                ref={ref}
            />
        </div>
    )
});

export default TextareaField;