import Book from "./Book.js";
import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import "./MyBooks.scss";
import SimpleModal from './SimpleModal';

const MyBooks = () => {

    const [myBooks, setMyBooks] = React.useState([]);
    const [showModal, setShowModal] = React.useState(false);
    const history = useHistory();

    const fetchBooks = () => {
        axios.get("/books").then(response => {
            setMyBooks(response.data)
        })
    }

    React.useEffect(() => {
        fetchBooks();
    }, [])

    const handleDelete = (bookId) => {
        axios.delete(`/books/${bookId}`).then(response => {
            fetchBooks()})
            .catch(error => {
                setShowModal(true);
        })
    }

    const handleCloseModal = () => {
        setShowModal(false);
    }

    return (
        <>
            <div>
                <h1>Mes livres</h1>
                <div className="list-container">
                    {myBooks.length !== 0 ? myBooks.map(book => (
                        <div key={book.id} className="mybook-container">
                            <Book title={book.title} category={book.category.label} />
                            <div className="container-buttons">
                                <button className="btn btn-primary btn-sm" onClick={() => history.push(`/addBook/${book.id}`)}>Modifier</button>
                                <button className="btn btn-primary btn-sm" type="button" onClick={() => handleDelete(book.id)}>Supprimer</button>
                            </div>
                        </div>
                    )) : "Vous n'avez pas déclaré de livres."}
                    <br />
                </div>
                <Link className="btn btn-primary btn-sm" to="/addBook">Nouveau livre</Link>
            </div>
            <SimpleModal title={"Suppression de livre impossible"} bodyTxt={"Livre en cours d'emprunt"} handleCloseModal={handleCloseModal} showModal = {showModal} />
        </>
    );
}

export default MyBooks;