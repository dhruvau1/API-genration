import React from 'react'
import { useState } from 'react';
import Columns from './Columns';
import Starter from './Starter';
import Services from './Services';

const Form = () => {
  const [columns, setColumns] = useState([]);
  const [services, setServices] = useState([]);
  return (
    <>
      <Starter />
      <Columns columns={columns} setColumns={setColumns} />
      <Services services={services} setServices={setServices} columns={columns}/>
    </>
  )
}

export default Form