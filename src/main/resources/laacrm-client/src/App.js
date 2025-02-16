import './App.css';
import {Container, createTheme, ThemeProvider} from "@mui/material";
import {Outlet, useNavigate} from "react-router-dom";
import Title from "./components/title/Title";
import SideBar from "./components/sideBar/SideBar";
import {useEffect, useState} from "react";
import apiEngine from "./api/apiEngine";
import authService from "./api/authService";
import {useDispatch} from "react-redux";
import {loadModules} from "./data/slice/moduleSlice";

const App = () => {

    const theme = createTheme({
        components: {
            MuiButton: {
                styleOverrides: {
                    root: {
                        backgroundColor: "#03346E",
                        "&.MuiButtonBase-root": {
                            "&:hover": {
                                backgroundColor: "rgba(3,52,110,0.81)"
                            }
                        }
                    }
                }
            }
        }
    });

    const nav = useNavigate();
    const dispatch = useDispatch();
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        authService.authenticateToken().then(() => {
            apiEngine.requestHelper("GET", "/v1/api/module").then((response) => {
                dispatch(loadModules(response.data.data.modules));
                setIsLoading(false);
            });
        }).catch(() => {
            authService.clearAuthData();
            nav("/login");
        })
    },[])

    return (
      <ThemeProvider theme={theme}>
          {isLoading ? (
              <Container maxWidth="" className={`app-parent-cont`}>
                  Loading....
              </Container>
          ) : (
              <Container maxWidth="" className={`app-parent-cont`}>
                  <Title/>
                  <Container maxWidth="" className={`d-flex`}>
                      <SideBar/>
                      <Outlet/>
                  </Container>
              </Container>
          )}
      </ThemeProvider>
    );
}

export default App;
