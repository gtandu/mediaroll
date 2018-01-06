import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { AuthentificationService } from '../../services/authentification.service';
import { FileUploader, FileUploaderOptions, FileItem, ParsedResponseHeaders } from 'ng2-file-upload';
import { Media } from '../../models/media';

@Component({
  selector: 'app-fileupload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css'],
})
export class FileUploadComponent implements OnInit, AfterViewChecked {

  mediasUploaded: Media[] = [];
  url: string;
  authToken: string;
  loading = false;

  public hasBaseDropZoneOver = false;
  public hasAnotherDropZoneOver = false;

  public uploader;

  options: FileUploaderOptions = {
    url: this.authService.server + '/medias',
    authToken: 'Token ' + this.authService.token,
    authTokenHeader: 'Authorization',
    autoUpload: false
  };

  constructor(private authService: AuthentificationService) { }

  ngOnInit() {
    $('.modal').modal();
    this.uploader = new FileUploader(this.options);
    this.uploader.onSuccessItem = (item, response, status, headers) => this.onSuccessItem(item, response, status, headers);
  }

  onNotify(mediaId: string): void {
    this.mediasUploaded = this.mediasUploaded.filter(function (el) {
      return el.id !== mediaId;
    });
  }

  ngAfterViewChecked() {
    this.openModal();
  }

  onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
    this.mediasUploaded.push(JSON.parse(response));
    this.loading = true;
  }

  openModal(){
    $('#inputFile').change(function(){
      $('#modal1').modal('open');
    });
  }

  public fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  public fileOverAnother(e: any): void {
    this.hasAnotherDropZoneOver = e;
  }

}
