import './App.css';
import {Container} from "@mui/material";
import Title from "./Title/Title";
import SideBar from "./SideBar/SideBar";
import {Outlet} from "react-router-dom";

const App = () => {
  return (
      <Container maxWidth="" className={`app-parent-cont`}>
          <Title/>
          <Container maxWidth="" className={`d-flex`}>
              <SideBar/>
              <Outlet/>
          </Container>
      </Container>
  )
}

export default App;
