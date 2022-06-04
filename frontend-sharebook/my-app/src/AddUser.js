import axios from 'axios';
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './AddUser.scss';
import SimpleModal from './SimpleModal';

class AddUser extends React.Component {

    constructor() {
        super();
        this.state = { userData: {}, showModal: false };
    }

    handleChange = (event) => {
        let currentState = { ...this.state.userData };
        currentState[event.target.name] = event.target.value;
        this.setState({ userData: currentState });
    }

    handleCloseModal = () => {
        this.setState({ showModal: false })
    }

    onSubmit = (event) => {
        event.preventDefault();
        axios.post('/users', {
            ...this.state.userData
        }).then(response => {
            this.props.setUserInfo(response.data.firstName + " " + response.data.lastName);
            this.props.history("/myBooks");
        }).catch(() => {
            this.setState({ showModal: true })
        })
    }

    render() {
        return (
            <>
                <div className="add-user-container">
                    <div>
                        <h1>M'inscrire</h1>
                        <div>
                            <form onSubmit={this.onSubmit}>
                                <div>
                                    <label>Email</label>
                                    <input name="email" type="email" className="form-control" onChange={this.handleChange} />
                                </div>
                                <div>
                                    <label>Nom</label>
                                    <input name="lastName" type="text" className="form-control" onChange={this.handleChange} />
                                </div>
                                <div>
                                    <label>Prénom</label>
                                    <input name="firstName" type="text" className="form-control" onChange={this.handleChange} />
                                </div>
                                <div>
                                    <label>Mot de passe</label>
                                    <input name="password" type="password" className="form-control" onChange={this.handleChange} />
                                </div>
                                <div className="container-valid text-center">
                                    <input type="submit" className="btn btn-primary" value="Valider" />
                                </div>
                            </form>
                        </div>
                        <div><Link to="/">Retour à l'accueil</Link></div>
                    </div>
                </div>
                <SimpleModal
                    title={"Mail déja utilisé"}
                    bodyTxt={"Cet email est déja utilisé, merci d'en saisir un autre"}
                    handleCloseModal={this.handleCloseModal}
                    showModal={this.state.showModal}
                ></SimpleModal>
            </>
        )
    }

}

// Wrap and export
export default function Wrapper(props) {
    const history = useNavigate();
    return <AddUser {...props} history={history} />;
  }