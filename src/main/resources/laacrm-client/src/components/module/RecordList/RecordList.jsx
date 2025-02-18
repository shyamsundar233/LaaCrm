import "./RecordList.css";
import {Box, Button, Container, Paper, Typography} from "@mui/material";
import {useNavigate, useParams} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {DataGrid} from "@mui/x-data-grid";
import apiEngine from "../../../api/apiEngine";
import {loadRecords} from "../../../data/slice/recordSlice";
import Banner from "../banner/Banner";

const RecordList = () => {

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {moduleName} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState("");
    const [columns, setColumns] = useState([]);
    const [columnVisibility, setColumnVisibility] = useState({});
    const [rows, setRows] = useState([]);
    const [noRecords, setNoRecords] = useState(true);

    useEffect(() => {
        resetData();
        let moduleObj = modules.find((item) => item.moduleName === moduleName);
        setModule(moduleObj);
        let columnsArr = [];
        let fields = moduleObj.layouts[0].fields;
        for(let index in fields) {
            let field = fields[index];
            columnsArr.push({field: field.fieldName, flex: 1});
            if(field.isVisible !== "true"){
                let obj = columnVisibility;
                obj[field.fieldName] = false;
                setColumnVisibility(obj);
            }
        }
        setColumns(columnsArr);
        apiEngine.requestHelper("GET", `/v1/api/${moduleObj.moduleId}/record`).then((res) => {
            dispatch(loadRecords(res.data.data.records));
            setIsLoading(false);
            if(res.data.data.records.length === 0){
                return;
            }else {
                setNoRecords(false);
            }
            let recordDetails = res.data.data.records.map(elem => {
                return {
                    ...elem.recordDetails,
                    id: elem.recordId
                };
            });
            setRows(recordDetails);
        })
    },[moduleName]);

    const resetData = () => {
        setIsLoading(true);
        setModule("");
        setColumns([]);
        setRows([]);
        setNoRecords(true);
    }

    if(isLoading) {
        return (<Box>Loading...</Box>)
    }else {
        if(noRecords){
            return (<Banner/>)
        }else {
            return (
                <Container maxWidth="">
                    <Box className={`p-1 mt-4`}>
                        <Box className={`d-flex`}>
                            <Typography className={`heading-1`}>All {module.pluralName}</Typography>
                            <Box className={`ms-auto me-3`}>
                                <Button variant={`contained`} className={`me-2`} onClick={() => navigate(`/app/module/${moduleName}/create`)}>Create {module.singularName}</Button>
                            </Box>
                        </Box>
                        <Paper className={`mt-3`} sx={{ height: "80vh", width: '100%' }}>
                            <DataGrid
                                columns={columns}
                                columnVisibilityModel={columnVisibility}
                                rows={rows}
                                checkboxSelection
                                sx={{ border: 0 }}
                            />
                        </Paper>
                    </Box>
                </Container>
            );
        }
    }
};

export default RecordList;
