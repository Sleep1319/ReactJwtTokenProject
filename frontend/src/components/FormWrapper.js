import React from 'react';

export default function FormWrapper({ children }) {
    return (
        <form>
            {children}
        </form>
    );
}