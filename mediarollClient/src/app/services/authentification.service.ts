import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class AuthentificationService {
    public token: string;
    public server: string;

    constructor(private http: Http) {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.token = currentUser && currentUser.token;
        this.server = 'http://localhost:8080';
    }

    login(mail: String, password: String): Observable<Boolean> {
        const body = JSON.stringify({ mail: mail, password: password });
        const headers = new Headers({ 'Content-Type': 'application/json' });
        const options = new RequestOptions({ headers: headers });

        return this.http.post(this.server+'/api-token', body, options)
            .map((response: Response) => {
                // login successful if there's a jwt token in the response
                console.log(response);
                let token = response.json().token;
                if (token) {
                    console.log(token);

                    // set token property
                    this.token = token;

                    // store username and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify({ mail: mail, token: token }));

                    // return true to indicate successful login
                    return true;
                }
            })
            .catch((error: any) => {
                return Observable.throw(error);
            });
    }

    logout(): void {
        // clear token remove user from local storage to log user out
        this.token = null;
        localStorage.removeItem('currentUser');
    }

}