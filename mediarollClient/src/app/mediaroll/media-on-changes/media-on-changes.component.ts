import { Component, OnInit, OnChanges, SimpleChanges, SimpleChange, Input} from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
@Component({
  selector: 'app-media-on-changes',
  templateUrl: './media-on-changes.component.html',
  styleUrls: ['./media-on-changes.component.css']
})
export class MediaOnChangesComponent implements OnInit, OnChanges {
  @Input() mediasUploaded: Media[];

  constructor(private mediaService: MediaService) { }

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges) { }

  showModal(el: HTMLImageElement) {
    console.log(el);
    $('#myModal').show();
    $('#modalContent').attr('src', el.src);
    $('#modalContent').attr('data-id', el.getAttribute('data-id'));
    $('#caption').html($(this).attr('alt'));
    $('.navbar').css('z-index', '0');
    $('#slide-out').css('z-index', '0');

  }

  closeModal() {
    $('#myModal').hide();
    $('.navbar').css('z-index', '');
    $('#slide-out').css('z-index', '');
  }

  deleteMedia() {
    const mediaId = $('#modalContent').attr('data-id');
    const isDeleted = this.mediaService.deleteMediaById(mediaId).subscribe(
      response => {
        console.log(response);
      },
      err => {
        console.log('Error occured.');
      }
    );
  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }
}
