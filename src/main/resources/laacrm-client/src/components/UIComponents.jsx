import {Alert, Button} from "@mui/material";
import ReactDOM from "react-dom/client";

export const XButton = ({label, variant, className, onClick}) => {

    let sx = {
        textTransform: "none",
        height: "35px",
        boxShadow: "rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px",
        cursor: "pointer",
    }

    if(variant === "outlined") {
        sx.border = "1px solid #03346E"
        sx.color = "#03346E"
    }

    if(variant === "contained") {
        sx.backgroundColor = "#03346E"
        sx.color = "white"
    }

    if(variant === "delete") {
        sx.color = "#D84040"
        sx.border = "1px solid #D84040"
    }

    return (<Button className={className} style={sx} onClick={onClick}>{label}</Button>)
}

export const UiUtils = {
    showAlert : (message, variant) => {
        let sx = {
            display: "flex",
            position: "absolute",
            top: "6px"
        }
        setTimeout(() => {
            document.getElementById("app-alert-elem").style.display = "none";
        },[3000]);

        const root = ReactDOM.createRoot(document.getElementById("app-alert"));
        root.render(<Alert id="app-alert-elem" style={sx} severity={variant}>{message}</Alert>);
    }
}