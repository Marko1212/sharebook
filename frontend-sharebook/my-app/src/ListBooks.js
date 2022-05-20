import React from 'react';
import Book from "./Book.js";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

import "./MyBooks.scss";
import "./ListBooks.scss";

class ListBooks extends React.Component {

    constructor() {
        super();
        this.state = {
            books: []
        }
    }

    componentDidMount() {
        axios.get('/books?status=FREE').then(response => {
            this.setState({books : response.data });
        })
    }

    borrowBook(bookId) {
        axios.post(`/borrows/${bookId}`, {}).then(() => {
            this.props.history.push('/myBorrows');           
        });
    }

    render() {
        return (
            <div>
                <h1>Livres disponibles</h1>
                <div className="list-container">
                    {this.state.books.length !== 0 ? this.state.books.map((book, key)=> (
                        <div key={key} className="list-book-container">
                            <Book title={book.title} category={book.category.label} lender= {`${book.user.firstName} ${book.user.lastName}`} />
                            <button className="btn btn-primary btn-sm" type="button" onClick={() => this.borrowBook(book.id)}>Emprunter</button>
                        </div>
                    )) : "Pas de livres disponibles"}
                </div>
            </div>
        );

    }
}

// Wrap and export
export default function Wrapper(props) {
    const history = useNavigate();
    return <ListBooks {...props} history={history} />;
  }