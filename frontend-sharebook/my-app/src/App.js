import { BrowserRouter, Route, Redirect, useHistory } from 'react-router-dom';
import React from 'react';
import ListBooks from './ListBooks';
import MyBooks from './MyBooks';
import AddBook from './AddBook';
import MyBorrows from './MyBorrows';
import Login from './Login';
import AddUser from './AddUser';
import Header from './Header';
import { useState, useEffect } from 'react';
import axios from 'axios';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

const UserConnected = ({ setUserInfo, userInfo }) => {
  const history = useHistory();
  useEffect(() => {
    axios.get('/isConnected').then(response => {
      if (history.location.pathname === "/") {
        history.push("/listBooks")
      }
      else {
        history.push(history.location.pathname)
      }

      setUserInfo(response.data)
    }, () => {
      console.error('user unknown')
      history.push("/login")
      setUserInfo(null)
    })
  }, [history, setUserInfo]);

  return (<>
    {userInfo && <Header userInfo={userInfo} setUserInfo={setUserInfo} />}
  </>
  )
}


function App() {

  const [userInfo, setUserInfo] = React.useState('');

  return (
    <div>
      <BrowserRouter>
        <div className="container">
          <UserConnected userInfo={userInfo} setUserInfo={setUserInfo} />
          <Route path="/listBooks">
            <ListBooks />
          </Route>
          <Route path="/myBooks">
            <MyBooks />
          </Route>
          <Route exact path="/addBook/:bookId">
            <AddBook />
          </Route>
          <Route exact path="/addBook">
            <AddBook />
          </Route>
          <Route path="/myBorrows">
            <MyBorrows />
          </Route>
          <Route path="/login">
            <Login setUserInfo={setUserInfo} />
          </Route>
          <Route path="/addUser">
            <AddUser setUserInfo={setUserInfo} />
          </Route>
        </div>
      </BrowserRouter>
    </div>
  );
  }

export default App;
