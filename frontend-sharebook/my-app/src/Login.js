import React from 'react';
import logo from './logo.jpg';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.scss';
import SimpleModal from './SimpleModal';

class Login extends React.Component {

    constructor() {
        super();
        this.state = { userData: {}, showModal: false }
        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);

    }

    handleChange(event) {
        let currentState = { ...this.state.userData };
        currentState[event.target.name] = event.target.value;
        this.setState({ userData: currentState });
    }

    onSubmit(event) {
        event.preventDefault();
        axios.post('/authenticate', {
            email: this.state.userData.email,
            password: this.state.userData.password
        }).then((response) => {
            this.props.setUserInfo(response.data.userName)
            this.props.history.push('/listBooks')
        }).catch((error) => {
            this.setState({showModal: true})
        });
    }

    handleCloseModal() {
        this.setState({showModal: false})
    }

    render() {

        const title = "Login incorrect"
        const bodyTxt = "Votre Login ou mot de passe est incorrect"

        return (
            <>
                <div className="login-container">
                    <div>
                        <div>
                            <img src={logo} alt="Logo" />
                        </div>
                        <div className="title">
                            Bienvenue sur Sharebook!
                        </div>
                        <div className="form-container">
                            <form onSubmit={this.onSubmit}>
                                <span>Email: </span>
                                <input type="email" className="form-control" name="email" onChange={this.handleChange} />
                                <span>Password: </span>
                                <input type="password" className="form-control" name="password" onChange={this.handleChange} />
                                <div>
                                    <input type="submit" className="btn btn-primary mt-3" value="OK" />
                                </div>
                            </form>
                        </div>
                        <div><Link to="/addUser">M'inscrire</Link></div>
                    </div>
                </div>
                <SimpleModal title = {title} bodyTxt = {bodyTxt} handleCloseModal = {this.handleCloseModal} showModal = {this.state.showModal} />
            </>
        )
    }

}

// Wrap and export
export default function Wrapper(props) {
    const history = useNavigate();
    return <Login {...props} history={history} />;
}