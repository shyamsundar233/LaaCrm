import "./RecordCreate.css";
import {Alert, Box, Button, Container, Grid2, MenuItem, Select, TextField, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import apiEngine from "../../../../api/apiEngine";

const RecordCreate = () => {

    const navigate = useNavigate();
    const {moduleName, layoutId} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState({});
    const [layout, setLayout] = useState([]);
    const [fields, setFields] = useState([]);
    const [recordDetails, setRecordDetails] = useState({});
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        let moduleObj = modules.find(elem => elem.moduleName === moduleName);
        if(moduleObj) {
            setModule(moduleObj);
            setIsLoading(false);
            let layoutObj = moduleObj.layouts.find(layout => layout.layoutId === layoutId);
            setLayout(layoutObj);
            setFields(layoutObj.fields);
        }
    },[modules, moduleName]);

    useEffect(() => {
        setTimeout(() => {
            setErrorMessage("");
        }, 3000)
    },[errorMessage]);

    const onFieldValueChange = (fieldName, fieldValue) => {
        let recordDtls = recordDetails;
        recordDtls[fieldName] = fieldValue;
        setRecordDetails(recordDtls);
    }

    const onRecordCreate = () => {
        if(validate()){
            let data = {
                moduleId: module.moduleId,
                layoutId: layout.layoutId,
                recordDetails: recordDetails
            }
            apiEngine.requestHelper("POST", `/v1/api/${module.moduleId}/record`, data).then(res => {
                navigate(`/app/module/${module.moduleName}`);
            })
        }
    }

    const validate = () => {
        for(let index = 0; index < fields.length; index++) {
            let field = fields[index];
            let props = field.fieldProperties;
            for(let index1 = 0; index1 < props.length; index1++) {
                let prop = props[index1];
                if(prop.propertyName === "isMandatory" && prop.propertyValue === 'true' && !recordDetails[field.fieldName]){
                    setErrorMessage(`${field.fieldName} is mandatory`);
                    return false;
                }else if(prop.propertyName === "maxChar" && recordDetails[field.fieldName]){
                    let maxChar = parseInt(prop.propertyValue);
                    if(recordDetails[field.fieldName].length > maxChar){
                        setErrorMessage(`Limit Exceeded, ${field.fieldName} allows only ${maxChar} characters`);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    return (
        <Container maxWidth="" className={`outlet-parent-cont`}>
            {errorMessage &&
                <Box className={`alert-pop-rec-create`}>
                    <Alert severity="error">{errorMessage}</Alert>
                </Box>
            }
            {isLoading ? (
                <Box>Loading....</Box>
            ) : (
                <Box className={`p-4 mt-3`}>
                    <Box className={`d-flex`}>
                        <Typography className={`heading-1`}>Create {module.singularName} - {layout.layoutName}</Typography>
                        <Box className={`ms-auto me-3`}>
                            <Button variant={`outlined`} className={`me-2`} onClick={() => navigate(`/app/module/${moduleName}/list`)}>Cancel</Button>
                            <Button variant={`contained`} onClick={onRecordCreate}>Create</Button>
                        </Box>
                    </Box>
                    <Box className={`mt-5`}>
                        <Grid2 container spacing={4}>
                            {fields.map((field, index) => (
                                <Grid2 size={6}>
                                    <Field field={field} fieldProps={field.fieldProperties} recordDetails={recordDetails} onValueChange={onFieldValueChange} />
                                </Grid2>
                            ))}
                        </Grid2>
                    </Box>
                </Box>
            )}
        </Container>
    )
}

const Field = ({field, fieldProps, recordDetails, onValueChange}) => {

    const [options, setOptions] = useState([]);
    const [defValue, setDefValue] = useState(null);
    const [isMandatory, setIsMandatory] = useState(false);

    useEffect(() => {
        setIsMandatory(fieldProps.find(prop => prop.propertyName === "isMandatory").propertyValue === "true");
       if(field.fieldType === '4'){
           setOptions(JSON.parse(fieldProps.find(prop => prop.propertyName === "options").propertyValue.replace(/'/g, '"')));
           setDefValue(fieldProps.find(prop => prop.propertyName === "defValue").propertyValue);
       }
    },[])


    const FieldElem = () => {
        switch (field.fieldType) {
            case '1':
            case '6':
            case '7':
                return <TextField
                    variant="outlined"
                    fullWidth
                    value={recordDetails[field.fieldName]}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />;
            case '3':
            case '9':
                return <TextField
                    variant="outlined"
                    type="number"
                    fullWidth
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            case '4':
                return <Select
                    fullWidth
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                    variant={`outlined`}
                    value={defValue}
                >
                    {options.map((option, index) => (
                        <MenuItem value={option}>{option}</MenuItem>
                    ))}
                </Select>
            case '8':
                return <TextField
                    variant="outlined"
                    type="email"
                    fullWidth
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            case '10':
                return <TextField
                    variant="outlined"
                    type="url"
                    fullWidth
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
        }
    }

    return (
        <Container key={`field_${field.fieldId}_elem`} className={`d-flex-a-center w-100`}>
            <Typography variant={`subtitle1`} style={{width: "30%"}}>
                {field.fieldName}
                {isMandatory ? <span className={`text-danger ms-1`}>*</span> : null}
            </Typography>
            <FieldElem/>
        </Container>
    )
}

export default RecordCreate;