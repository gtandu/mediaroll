import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { AuthentificationService } from '../authentification.service';
import { Media } from '../../models/media';
import { Paths } from '../../models/paths';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';


@Injectable()
export class MediaService {

  constructor(private http: Http, private authService: AuthentificationService) { }

  getAllMedias(): Observable<Media[]> {
            const headers = new Headers({ 'Authorization': 'Token ' + this.authService.token});
            const options = new RequestOptions({ headers: headers });
            return this.http.get(this.authService.server + Paths.MEDIAS, options)
                .map((response: Response) => response.json());
  }

  buildUrlToMediaWithId(id: string){
    return this.authService.server + Paths.MEDIAS + '/' + id + '/response';
  }

}
