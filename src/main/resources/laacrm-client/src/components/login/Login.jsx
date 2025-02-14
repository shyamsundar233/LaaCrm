import "./Login.css";
import {Box, Button, Container, createTheme, TextField, ThemeProvider} from "@mui/material";
import {useState} from "react";
import axios from "axios";
import authService from "../../api/authService";

const loginFieldsTheme = createTheme({
    components: {
        MuiTextField: {
            styleOverrides: {
                root: {
                    "& .MuiInputLabel-root": {
                        color: "white"
                    },
                    "& .MuiFocused": {
                        color: "white"
                    }
                }
            }
        }
    }
});

const Login = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = () => {
        axios.post("/v1/api/users/login", {username, password}).then(res => {
            authService.setAuthData(res.data.data.token, username);
        })
    }

    return (
        <Container maxWidth="lg">
            <Box className={`login-box-container`}>
                <Box className={`login-form-container`}>
                    <Box className={`mt-4 d-flex-j-center h3`}>
                        Welcome to Laa CRM !!!
                    </Box>
                    <Box className={`mt-4 h5 d-flex-j-center`}>
                        Login Here to continue
                    </Box>
                    <Box className={`mt-4 d-flex-j-center flex-column p-5`}>
                        <ThemeProvider theme={loginFieldsTheme}>
                            <TextField id="standard-basic" label="Username/Email" variant="outlined" onChange={e => setUsername(e.target.value)}/><br/>
                            <TextField id="standard-basic" label="Password" variant="outlined" type="password" onChange={e => setPassword(e.target.value)}/>
                            <Button onClick={handleLogin} variant={`contained`} className={`mt-4`}>Log In</Button>
                        </ThemeProvider>
                    </Box>
                </Box>
            </Box>
        </Container>
    )
}

export default Login;