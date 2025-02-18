import {createSlice} from "@reduxjs/toolkit";

const recordSlice = createSlice({
    name: 'record',
    initialState: {
        records: {}
    },
    reducers: {
        loadRecords: (state, action) => {
            state.records = action.payload;
        },
        isRecordPresent: (state) => {

        }
    }
});

export const { loadRecords } = recordSlice.actions;

export default recordSlice.reducer;