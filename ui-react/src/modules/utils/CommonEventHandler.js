
function textFieldChange(evt) {
    let fld = evt.target;
    this.setState({[fld.name]:fld.value});
};


export {textFieldChange};
