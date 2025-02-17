import "./Login.css";
import {Box, Button, Container, createTheme, Grid2, TextField, ThemeProvider, Link} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import authService from "../../api/authService";
import {useNavigate} from "react-router-dom";
import apiEngine from "../../api/apiEngine";

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

const Login = ({operation}) => {

    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    const handleLogin = () => {
        axios.post("/v1/api/users/login", {username, password}).then(res => {
            authService.setAuthData(res.data.data.token, username);
            if(!res.data.data.initialized){
                apiEngine.requestHelper("GET", "/v1/api/initPopulate").then(() => {
                    navigate("/app/dashboard");
                })
            }
        })
    }

    const handleRegister = () => {
        if(!validate()){
            return false;
        }
        let data = {
            userName: username,
            password: password,
            email: email,
            firstName: firstName,
            lastName: lastName,
        }
        axios.post("/v1/api/users/saveUser", data).then(res => {
            navigate("/login");
        }).catch(err => {
            alert(err.response.data.message);
        })
    }

    const validate = () => {
        if(password !== confirmPassword){
            return false;
        }
        return true;
    }

    useEffect(() => {
        authService.authenticateToken().then(res => {
            navigate("/app/dashboard");
        }).catch(err => {

        })
    },[])

    return (
        <Container maxWidth="lg">
            <Box className={`login-box-container`}>
                <Box className={`login-form-container`} style={{
                    height: operation === 'login' ? `380px` : `480px`
                }}>
                    <Box className={`mt-4 d-flex-j-center h3`}>
                        Welcome to Laa CRM !!!
                    </Box>
                    <Box className={`mt-4 h5 d-flex-j-center`}>
                        {operation === "login" ? "Login Here to continue" : "Register Here to continue"}
                    </Box>
                    <Box className={`d-flex-j-center flex-column p-3 ps-4 pe-4`}>
                        <ThemeProvider theme={loginFieldsTheme}>
                            {operation === "login" ? (
                                <>
                                    <TextField id="standard-basic" label="Username/Email" variant="outlined" onChange={e => setUsername(e.target.value)}/><br/>
                                    <TextField id="standard-basic" label="Password" variant="outlined" type="password" onChange={e => setPassword(e.target.value)}/>
                                    <Button onClick={handleLogin} variant={`contained`} className={`mt-4`}>Log In</Button>
                                    <Link className={`mt-3 text-center`}  href={`/register`}>Click Here to Register</Link>
                                </>
                            ): (
                                <>
                                    <Grid2 container spacing={4}>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="Username" variant="outlined" onChange={e => setUsername(e.target.value)}/><br/>
                                        </Grid2>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="Email" variant="outlined" onChange={e => setEmail(e.target.value)}/><br/>
                                        </Grid2>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="firstName" variant="outlined" onChange={e => setFirstName(e.target.value)}/><br/>
                                        </Grid2>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="lastName" variant="outlined" onChange={e => setLastName(e.target.value)}/><br/>
                                        </Grid2>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="Password" variant="outlined" type="password" onChange={e => setPassword(e.target.value)}/>
                                        </Grid2>
                                        <Grid2 className={`d-flex-j-center`} size={6}>
                                            <TextField id="standard-basic" label="Confirm Password" variant="outlined" type="password" onChange={e => setConfirmPassword(e.target.value)}/>
                                        </Grid2>
                                    </Grid2>
                                    <Button onClick={handleRegister} variant={`contained`} className={`mt-4`}>Sign In</Button>
                                    <Link className={`mt-3 text-center`} href={`/login`}>Click Here to Login</Link>
                                </>

                            )}
                        </ThemeProvider>
                    </Box>
                </Box>
            </Box>
        </Container>
    )
}

export default Login;