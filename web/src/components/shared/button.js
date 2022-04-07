import React from "react";

export function Button(props) {
    if (props.mode === 'view') {
        return '';
    }
    return (
        <button
            type={props.type}
            className={props.className}
            onClick={props.onClick}
            disabled={props.disabled}
        >
            {props.text}
        </button>
    );
}