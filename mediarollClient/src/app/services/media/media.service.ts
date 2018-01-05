import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { AuthentificationService } from '../authentification.service';
import { Media } from '../../models/media';
import { Picture } from '../../models/picture';
import { Video } from '../../models/video';
import { Paths } from '../../models/paths';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';


@Injectable()
export class MediaService {

  headers = new Headers({ 'Authorization': 'Token ' + this.authService.token });
  options = new RequestOptions({ headers: this.headers });

  constructor(private http: Http, private authService: AuthentificationService) {
  }

  getAllMedias(): Observable<Media[]> {
    return this.http.get(this.authService.server + Paths.MEDIAS, this.options)
      .map((response: Response) => response.json());
  }

  getAllPictures(): Observable<Picture[]> {
    return this.http.get(this.authService.server + Paths.PICTURES, this.options).map((response: Response) => response.json());
  }

  getAllVideos(): Observable<Video[]> {
    return this.http.get(this.authService.server + Paths.VIDEOS, this.options).map((response: Response) => response.json());
  }

  deleteMediaById(mediaId: string) {
    return this.http.delete(this.authService.server + Paths.MEDIAS + `/${mediaId}`, this.options);
  }

  buildUrlToMediaWithId(mediaId: string) {
    return this.authService.server + Paths.MEDIAS + `/${mediaId}/response`;
  }

}
