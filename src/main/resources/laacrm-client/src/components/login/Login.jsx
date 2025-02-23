import "./Login.css";
import {Box, Container, Grid2, TextField, Link} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import authService from "../../api/authService";
import {useNavigate} from "react-router-dom";
import apiEngine from "../../api/apiEngine";
import {UiUtils, XButton} from "../UIComponents";

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
            }else {
                navigate("/app/dashboard");
            }
        }).catch(err => {
            let message;
            let error = err.response.data.message;
            switch (error){
                case "Bad credentials":
                    message = "Enter valid credentials !!!";
                    break;
            }
            UiUtils.showAlert(message, "error");
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
            UiUtils.showAlert(err.response.data.message, "error");
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
        <Container maxWidth="">
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
                        {operation === "login" ? (
                            <>
                                <TextField id="standard-basic" label="Username/Email" variant="outlined" onChange={e => setUsername(e.target.value)}/><br/>
                                <TextField id="standard-basic" label="Password" variant="outlined" type="password" onChange={e => setPassword(e.target.value)}/>
                                <XButton label="Log In" variant="contained" onClick={handleLogin} className={`mt-4`}/>
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
                                <XButton label="Sign In" variant="contained" onClick={handleLogin} className={`mt-4`}/>
                                <Link className={`mt-3 text-center`} href={`/login`}>Click Here to Login</Link>
                            </>

                        )}
                    </Box>
                </Box>
            </Box>
        </Container>
    )
}

export default Login;