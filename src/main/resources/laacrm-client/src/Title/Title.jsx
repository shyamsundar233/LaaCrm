import "./Title.css";
import {Container, Typography} from "@mui/material";

const Title = () => (
    <Container className={`ps-3 title-parent-cont d-flex align-items-center`} maxWidth="">
        <Typography color={`white`} variant={'h5'}>Laa CRM</Typography>
    </Container>
)

export default Title;