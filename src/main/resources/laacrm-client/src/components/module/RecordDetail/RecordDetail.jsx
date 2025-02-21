import "./RecordDetail.css";
import {Box, Button, Container, Grid2} from "@mui/material";
import {useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import apiEngine from "../../../api/apiEngine";
import LeftArrow from "../../../components/asset/left-arrow.png";
import {XButton} from "../../UIComponents";

const RecordDetail = () => {

    const navigate = useNavigate();
    const {moduleName, recordId} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState(null);
    const [record, setRecord] = useState(null);
    const [fields, setFields] = useState([]);
    const [recordName, setRecordName] = useState("");
    const [recordDetails, setRecordDetails] = useState({});

    useEffect(() => {
        let moduleObj = modules.find(item => item.moduleName === moduleName);
        setModule(moduleObj);
        let fieldsArr = moduleObj.layouts[0].fields;
        setFields(fieldsArr);
        let recNameField = fieldsArr.find(fld => fld.fieldType === '6');
        apiEngine.requestHelper("GET", `/v1/api/${moduleObj.moduleId}/record/${recordId}`).then((response) => {
            let recordObj = response.data.data.record;
            setRecord(recordObj);
            setRecordName(recordObj.recordDetails[recNameField.fieldName]);
            setRecordDetails(recordObj.recordDetails);
            setIsLoading(false);
        });
    },[moduleName, recordId])

    if(isLoading){
        return (<Container maxWidth="">Loading...</Container>);
    }else {
        return (
            <Container maxWidth="" className={`ps-0 pe-0`}>
                <Box className={`mt-4 mb-4 ps-2 heading-1 pb-4 border-bottom d-flex-a-center`}>
                    <img onClick={() => navigate(`/app/module/${moduleName}/list`)} src={LeftArrow} alt="left" className={`back-arr-detail`}/>
                    {recordName}
                    <Box className={`ms-auto me-4`}>
                        <XButton label="Edit" className={`me-3`} variant="contained" onClick={() => navigate(`/app/module/${moduleName}/${recordId}/edit`)}/>
                        <XButton label="Delete" variant="delete"/>
                    </Box>
                </Box>
                <Grid2 container spacing={4} className={`pt-2 ms-5`}>
                    {fields.map((field) => (
                        <Grid2 size={6}>
                            <Box className={`d-flex w-100`}>
                                <Box className={`w-25 fw-bold`}>{field.fieldName}</Box>
                                <Box className={`ms-4 w-75`}>{recordDetails[field.fieldName]}</Box>
                            </Box>
                        </Grid2>
                    ))}
                </Grid2>
            </Container>
        );
    }
};

export default RecordDetail;
