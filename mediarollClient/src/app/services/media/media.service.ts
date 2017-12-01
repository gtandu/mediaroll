import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { HttpHeaders, HttpParams, HttpErrorResponse } from "@angular/common/http";
import { AuthentificationService } from "../authentification.service";
import { Media } from '../../models/media';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map'

@Injectable()
export class MediaService {

  constructor(private http: Http, private authService: AuthentificationService) { }

  getAllMedias(): Observable<Media[]> {
            let headers = new Headers({ 'Authorization': 'Token ' + this.authService.token});
            let options = new RequestOptions({ headers: headers });
            return this.http.get(this.authService.server+'/medias', options)
                .map((response: Response) => response.json());
        }

}
