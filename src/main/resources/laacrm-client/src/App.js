import './App.css';
import {Container} from "@mui/material";
import {Outlet} from "react-router-dom";
import Title from "./components/title/Title";
import SideBar from "./components/sideBar/SideBar";
import {useDispatch} from "react-redux";
import {useEffect} from "react";
import apiEngine from "./api/apiEngine";

const App = () => {

    const dispatch = useDispatch();

    useEffect(() => {
        apiEngine.requestHelper("GET", "/v1/api/module").then((response) => {
            debugger
        });
    },[])

    return (
      <Container maxWidth="" className={`app-parent-cont`}>
          <Title/>
          <Container maxWidth="" className={`d-flex`}>
              <SideBar/>
              <Outlet/>
          </Container>
      </Container>
    );
}

export default App;
